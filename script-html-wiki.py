import os
import re
from markdown import markdown
from bs4 import BeautifulSoup
from pygments import highlight
from pygments.lexers import PythonLexer
from pygments.formatters import HtmlFormatter
from pygments.lexers import get_lexer_by_name, guess_lexer

INPUT_MD = 'pacman-backend.md'
OUTPUT_DIR = 'output-pacman-backend'

# Nettoyer titre en id ancre (slugify basique)
def slugify(text):
    text = text.lower()
    text = re.sub(r'[^\w\s-]', '', text)
    text = re.sub(r'\s+', '-', text)
    return text.strip('-')

# Lire fichier et découper en chapitres (#)
def parse_markdown(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    chapters = []
    in_code_block = False
    current_chapter = None

    for line in lines:
        stripped_line = line.strip()

        # Détecter les délimiteurs de bloc de code
        if stripped_line.startswith("```"):
            in_code_block = not in_code_block
            if current_chapter:
                current_chapter['content'].append(line)
            continue

        # Pendant un bloc de code, ajouter la ligne sans la filtrer
        if in_code_block:
            if current_chapter:
                current_chapter['content'].append(line)
            continue

        # Ignorer les images Markdown
        if re.match(r'!\[.*\]\(.*\)', stripped_line):
            continue

        # Nouveau chapitre = titre de niveau 1
        if stripped_line.startswith('# '):
            if current_chapter:
                chapters.append(current_chapter)
            current_chapter = {
                'title': stripped_line.strip('# ').strip(),
                'content': []
            }
        elif current_chapter:
            current_chapter['content'].append(line)

    # Ajouter le dernier chapitre
    if current_chapter:
        chapters.append(current_chapter)

    return chapters


# Extraire titres secondaires (##, ###) et générer TOC locale html
def generate_local_toc(html_content):
    soup = BeautifulSoup(html_content, 'html.parser')
    toc = []
    for header in soup.find_all(['h2', 'h3', 'h4', 'h5', 'h6']):
        header_id = header.get('id')
        if not header_id:
            header_id = slugify(header.text)
            header['id'] = header_id
        toc.append((header.name, header.text, header_id))

    if not toc:
        return ''

    toc_html = '<nav class="local-toc"><ul>'
    prev_level = 2
    for tag, text, hid in toc:
        level = int(tag[1])
        while prev_level < level:
            toc_html += '<ul>'
            prev_level += 1
        while prev_level > level:
            toc_html += '</ul>'
            prev_level -= 1
        toc_html += f'<li><a href="#{hid}">{text}</a></li>'
    while prev_level > 2:
        toc_html += '</ul>'
        prev_level -= 1
    toc_html += '</ul></nav>'
    return toc_html, str(soup)

# Fixe les problématiques de tableaux
def fix_tables_formatting(md_lines):
    fixed = []
    in_table = False

    for i, line in enumerate(md_lines):
        is_table_line = bool(re.match(r'^\s*\|.*\|\s*$', line))

        if is_table_line and not in_table:
            if fixed and fixed[-1].strip() != '':
                fixed.append('\n')  # ligne vide avant
            in_table = True

        elif not is_table_line and in_table:
            if fixed and fixed[-1].strip() != '':
                fixed.append('\n')  # ligne vide après
            in_table = False

        fixed.append(line)

    if in_table and fixed[-1].strip() != '':
        fixed.append('\n')

    return fixed

# Fixe les problématiques de tableaux
def prepare_markdown_text(md_lines):
    lines = fix_tables_formatting(md_lines)
    cleaned = []
    for line in lines:
        # Dé-échappe les caractères Markdown dans les tableaux
        if line.strip().startswith('|'):
            line = line.replace('\\_', '_')
        cleaned.append(line)
    return ''.join(cleaned)

# Corrige les indentations pour les parties de code
def corriger_blocs_code_mal_indentés(markdown):
    lignes = markdown.splitlines()
    resultat = []
    dans_bloc_code = False
    bloc_temp = []
    indentation_bloc = ""
    
    for i, ligne in enumerate(lignes):
        # Détection d’un début de bloc de code dans une liste
        if re.match(r"^\s*[-*]\s+.*$", ligne) and i + 1 < len(lignes):
            prochaine_ligne = lignes[i + 1]
            match_code = re.match(r"^\s+```(\w*)\s*$", prochaine_ligne)
            if match_code:
                resultat.append(ligne.strip())
                resultat.append("")  # Ligne vide avant bloc
                resultat.append(f"```{match_code.group(1)}")
                dans_bloc_code = True
                indentation_bloc = re.match(r"^(\s+)```", prochaine_ligne).group(1)
                continue
        
        if dans_bloc_code:
            if re.match(rf"^{indentation_bloc}```", ligne):
                resultat.append("```")  # Fin du bloc
                resultat.append("")     # Ligne vide après bloc
                dans_bloc_code = False
                continue
            else:
                ligne_sans_indent = ligne[len(indentation_bloc):] if ligne.startswith(indentation_bloc) else ligne
                resultat.append(ligne_sans_indent)
        else:
            resultat.append(ligne)

    return "\n".join(resultat)

# Générer menu latéral avec tous les chapitres
def generate_sidebar(chapters):
    html = '<nav class="sidebar"><ul>'
    for i, chap in enumerate(chapters):
        filename = f'chapter_{i+1}.html'
        html += f'<li><a href="{filename}">{chap["title"]}</a></li>'
    html += '</ul></nav>'
    return html

# Coloration syntaxe pour les parties de code
def highlight_code_blocks(html):
    soup = BeautifulSoup(html, 'html.parser')
    formatter = HtmlFormatter()

    for pre in soup.find_all('pre'):
        code = pre.code
        if not code:
            continue

        # Langage spécifié ?
        lang_class = code.get('class')
        try:
            if lang_class and len(lang_class) > 0:
                lang = lang_class[0].replace('language-', '')
                lexer = get_lexer_by_name(lang)
            else:
                lexer = guess_lexer(code.text)
        except:
            continue  # Ne pas coloriser si langage inconnu

        highlighted = highlight(code.text, lexer, formatter)
        pre.replace_with(BeautifulSoup(highlighted, 'html.parser'))

    return str(soup)

# Générer une page HTML complète
def generate_page(title, sidebar_html, toc_html, body_html):
    # Générer le CSS de Pygments
    pygments_css = HtmlFormatter(style='friendly').get_style_defs('.codehilite')
    return f'''<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>{title}</title>
<style>
 body {{ margin:0; font-family: Arial,sans-serif; display: block; overflow-x: hidden;}}
  .sidebar {{ display:none; }}
  main {{ margin-left: 350px; padding: 2em; }}
  .local-toc {{ position: fixed; left: 10px; top: 1em; width: 300px; background: #fafafa; 
   border: 1px solid #ddd; padding: 1em; max-height: 90vh; overflow-y: auto; font-size: 0.9em; }}
  .local-toc ul {{ list-style: none; padding-left: 1em; }}
  .local-toc li {{ margin: 0.3em 0; }}
  .local-toc a {{ text-decoration: none ; color: #1A5276}}
  .local-toc a:visited {{ color: #1A5276;}}
  .local-toc a:hover {{ color: #5499c7; text-decoration: none;}}
  table {{ border-collapse: collapse; width: 100%; margin: 1.5em 0; font-size: 0.95em; background-color: #fff; border: 1px solid #ccc;}}
  thead {{ background-color: #f2f2f2;}}
  th, td {{ border: 1px solid #ccc; padding: 0.6em 0.8em; text-align: left; vertical-align: top;}}
  tr:nth-child(even) {{ background-color: #f9f9f9;}}
  pre {{background: #f5f5f5; border: 1px solid #ccc; padding: 0.3em; border-radius: 4px;}}
  img {{ max-width: 100%; height: auto; border: 1px solid #000000; border-radius: 4px; box-shadow: 2px 2px 6px rgba(0,0,0,0.1);
   margin: 1em 0; display: block; padding: 5px; background-color: white;}}
  /* Styles Pygments pour le code colorisé */
  {pygments_css}
</style>
</head>
<body>
{sidebar_html}
<main>
<h1>{title}</h1>
{body_html}
</main>
{toc_html}
</body>
</html>'''

def main():
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    chapters = parse_markdown(INPUT_MD)
    sidebar_html = generate_sidebar(chapters)

    for i, chap in enumerate(chapters):
        # Convertir markdown en HTML
        md_lines = fix_tables_formatting(chap['content'])
        md_text = prepare_markdown_text(md_lines)
        #md_text = '\n'.join(chap['content'])
        html_content = markdown(md_text, extensions=['fenced_code', 'tables', 'toc', 'codehilite'])
        # Générer TOC locale + contenu avec ancres
        toc_html, html_with_ids = generate_local_toc(html_content)
        # Faire la coloration syntaxique pour les parties de code
        #html_with_ids = highlight_code_blocks(html_with_ids)
        # Générer page complète
        page_html = generate_page(chap['title'], sidebar_html, toc_html, html_with_ids)
        # Sauvegarder
        filename = os.path.join(OUTPUT_DIR, f'chapter_{i+1}.html')
        with open(filename, 'w', encoding='utf-8') as f:
            f.write(page_html)

    print(f"Site généré dans {OUTPUT_DIR}")

if __name__ == '__main__':
    main()
