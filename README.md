- Charger python sur le site [pyhton.org](https://www.python.org/)
- Créer un répertoire de sortie : output-pacman-backend
- intaller les différents modules avec la commande pip :
   - pip install markdown
   - pip install beautifulsoup4
   - pip install pygments
- **sinon devrait être possible avec cette seule commande** : pip install markdown beautifulsoup4 pygments
- lancer le script : pyhton script-html-wiki.py
 
Détail des modules :

- markdown : pour convertir du texte Markdown en HTML.
- beautifulsoup4 : fournit BeautifulSoup pour parser et manipuler du HTML.
- pygments : pour la coloration syntaxique de code source (highlight, get_lexer_by_name, guess_lexer, etc.).

Si besoin, modifier les paramètres en début de script : 
- INPUT_MD = 'pacman-backend.md'
- OUTPUT_DIR = 'output-pacman-backend'


