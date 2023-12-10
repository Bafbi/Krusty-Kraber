# Krusty Kraber

Une application de gestion de restaurant.

Capable de : 
- Gérer les stocks
- Gérer les commandes
- Gérer les employés
- Gérer les clients

## Installation

```bash
git clone https://github.com/Bafbi/Krusty-Kraber.git
```

## Usage // still in dev

```bash
cd Krusty-Kraber
gradle run
```

## Fonctionnement

### Architecture

L'application est basée sur une classe principale `Restaurant` qui contient les différentes listes d'objets (employés, transaction, stocks, ...) qui est completement séparée de l'interface graphique.
Puis d'une classe `Application` qui introduit un serveur web qui va nous permettre de communiquer avec l'interface graphique.

### Interface

Pour l'interface nous utilisons directement le web browser, pour cela nous avons un serveur web à l'aide de la librairie [Javalin](https://javalin.io/), et utilisons [</> htmx](https://htmx.org/) pour l'interativité de notre front-end sans avoir besoin d'écrire de javascript.
Notre html est généré à l'aide de [J2Html](https://j2html.com/) qui nous permet de générer du html à partir de java, [Tailwind CSS](https://tailwindcss.com/) à aussi été utilisé pour simplifier le css.
Cela nous permet d'avoir une interface graphique qui est complètement séparée de notre application, et qui coté developpement est plus simple à gérer car tout dans notre java et pas de javascript.

### L'application

Notre classe `Restaurant` contient les différentes 'modules' dont nous avons besoin pour faire fonctionner notre restaurant:
- `Stock` qui contient les différents `Ingredient` et les quantités disponibles, ainsi que un stock par défaut qui nous permet de reremplir les stocks.
- `EmployeManager` qui contient les différents `Employe` et si ils sont planifiés ou non.
- `TransactionManager` qui contient les différentes `Transaction` et leur `Command` associée.
- `List<Recette>` qui contient les différentes `Recette` du restaurant.

Les `Command` fonctionne à l'aide de plusieurs Map<?, Interger> qui va contenir les Recette/Boisson et leur quantité ainsi que les Recette/Boisson qui ont etait preparé.



