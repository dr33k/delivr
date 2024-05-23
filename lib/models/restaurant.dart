import 'package:delivr/models/food.dart';
import 'package:flutter/cupertino.dart';

class Restaurant extends ChangeNotifier{
  final List<Food> menu = [
    Food(
      name: 'Classic Cheeseburger',
      description: 'A juicy beef patty with melted cheddar, lettuce tomatoes , onions and pickles',
      imagePath: 'lib/images/burgers/b4.jpg',
      price: 4.99,
      category: FoodCategory.burgers,
      availableAddons: [
        Addon(name: 'Extra Cheese', price: 0.99),
        Addon(name: 'Bacon', price: 1.99),
        Addon(name: 'Avocado', price: 2.99),
      ]
    ),
    Food(
      name: 'Veggie Burger',
      description: 'A burger for vegetarians lettuce tomatoes , onions and pickles',
      imagePath: 'lib/images/burgers/b2.jpeg',
      price: 3.99,
      category: FoodCategory.burgers,
      availableAddons: [
        Addon(name: 'Extra Lettuce', price: 0.99),
        Addon(name: 'Avocado', price: 2.99),
      ]
    ),
    Food(
      name: 'Krabby Patty',
      description: 'A burger composing of ham, onions and pickles. Topped with soft fresh bread and ordered with a side of drinks',
      imagePath: 'lib/images/burgers/b3.jpg',
      price: 1.99,
      category: FoodCategory.burgers,
      availableAddons: [
        Addon(name: 'Mayonnaise', price: 1.99),
      ]
    ),
    Food(
      name: 'Beef Burger',
      description: 'Exquisite burger with spicy beef as the main ingredient.'+'Hot steamy meat dripping with sauce',
      imagePath: 'lib/images/burgers/b4.jpg',
      price: 4.99,
      category: FoodCategory.burgers,
      availableAddons: [
        Addon(name: 'Bacon', price: 1.99),
      ]
    ),
    Food(
      name: 'Krabby Patty Deluxe',
      description: 'A juicy chicken patty with melted cheddar, lettuce tomatoes , onions and pickles',
      imagePath: 'lib/images/burgers/b5.png',
      price: 4.99,
      category: FoodCategory.burgers,
      availableAddons: [
        Addon(name: 'Extra Chicken', price: 3.99),
      ]
    ),

    //drinks
    Food(
      name: 'Orange Juice',
      description: 'Pulpy orange drink with lots of flavour.Good to drink with lots of nutrients',
      imagePath: 'lib/images/drinks/dr1.jpg',
      price: 4.99,
      category: FoodCategory.drinks,
      availableAddons: [
        Addon(name: 'Wavy straw', price: 0.99),
        Addon(name: 'Extra Ice', price: 0.99),
      ]
    ),
    Food(
      name: 'Kiwi Drink',
      description: 'Unique and tasty smoothie made from the Kiwi fruit.Good to drink with lots of nutrients',
      imagePath: 'lib/images/drinks/dr2.jpg',
      price: 5.99,
      category: FoodCategory.drinks,
      availableAddons: [
        Addon(name: 'Wavy straw', price: 0.99),
        Addon(name: 'Extra Ice', price: 0.99),
      ]
    ),
    Food(
      name: 'Lemonade',
      description: 'Timeless classic drink to sip on a hot day. Lemonade is sweet, refreshing, good to drink with lots of nutrients',
      imagePath: 'lib/images/drinks/dr3.jpg',
      price: 2.99,
      category: FoodCategory.drinks,
      availableAddons: [
        Addon(name: 'Wavy straw', price: 0.99),
        Addon(name: 'Extra Ice', price: 0.99),
      ]
    ),
    Food(
      name: 'Black Coffee',
      description: 'Made from the coffee bean, hot, instant and can be taken with cream or sugar',
      imagePath: 'lib/images/drinks/dr4.jpg',
      price: 4.00,
      category: FoodCategory.drinks,
      availableAddons: [
        Addon(name: 'Extra Cream', price: 2.99),
      ]
    ),
    Food(
      name: 'Cocktail',
      description: 'Cocktail drink',
      imagePath: 'lib/images/drinks/dr5.jpg',
      price: 5.99,
      category: FoodCategory.drinks,
      availableAddons: [
        Addon(name: 'Wavy straw', price: 0.99),
        Addon(name: 'Extra Ice', price: 0.99),
      ]
    ),

    //desserts
    Food(
        name: 'Fruitcake',
        description: 'Dense, sweet bread filled with dried fruits and nuts.',
        imagePath: 'lib/images/desserts/d1.png',
        price: 4.99,
        category: FoodCategory.desserts,
        availableAddons: [
          Addon(name: 'Extra strawberries', price: 10),
          Addon(name: 'Vanilla Cream', price: 1.99),
        ]
    ),
    Food(
        name: 'Chocolate Chip Cookies',
        description: 'Warm, chewy treats with sweet chocolate chunks.',
        imagePath: 'lib/images/desserts/d2.jpg',
        price: 5.00,
        category: FoodCategory.desserts,
        availableAddons: [
          Addon(name: 'Hewet beans', price: 3),
        ]
    ),
    Food(
        name: 'Ice Cream',
        description: 'Creamy, sweet, and cold, a delightful treat.',
        imagePath: 'lib/images/desserts/d3.png',
        price: 10.99,
        category: FoodCategory.desserts,
        availableAddons: [
          Addon(name: 'Vanilla', price: 5),
          Addon(name: 'Chocolate', price: 5),
          Addon(name: 'Strawberry', price: 5),
          Addon(name: 'Banana', price: 5),
          Addon(name: 'Rocky Road', price: 5),
        ]
    ),
    Food(
        name: 'Cupcake',
        description: 'Sweet, fluffy, and colorful, topped with creamy frosting.',
        imagePath: 'lib/images/desserts/d4.jpg',
        price: 9.99,
        category: FoodCategory.desserts,
        availableAddons: [
          Addon(name: 'Extra strawberries', price: 10),
          Addon(name: 'Vanilla Cream', price: 1.99),
        ]
    ),
    Food(
        name: 'Chocolate',
        description: 'Dense choco beans, sweet and may be filled with dried fruits and nuts.',
        imagePath: 'lib/images/desserts/d5.png',
        price: 4.99,
        category: FoodCategory.desserts,
        availableAddons: []
    ),

    //salads
    Food(
        name: 'Caesar Salad',
        description: 'A Fresh mix of greens, veggies, and tangy dressing.',
        imagePath: 'lib/images/salads/s1.jpg',
        price: 4.99,
        category: FoodCategory.salads,
        availableAddons: []
    ),
    Food(
        name: 'Greek Salad',
        description: 'B Fresh mix of greens, veggies, and tangy dressing.',
        imagePath: 'lib/images/salads/s2.jpg',
        price: 2.99,
        category: FoodCategory.salads,
        availableAddons: []
    ),
    Food(
        name: 'Quinola Salad',
        description: 'C Fresh mix of greens, veggies, and tangy dressing.',
        imagePath: 'lib/images/salads/s3.jpg',
        price: 5.99,
        category: FoodCategory.salads,
        availableAddons: []
    ),
    Food(
        name: 'Asian Sesame Salad',
        description: 'D Fresh mix of greens, veggies, and tangy dressing.',
        imagePath: 'lib/images/salads/s4.jpg',
        price: 3.99,
        category: FoodCategory.salads,
        availableAddons: []
    ),
    Food(
        name: 'Southwest Salad',
        description: 'E Fresh mix of greens, veggies, and tangy dressing.',
        imagePath: 'lib/images/salads/s5.png',
        price: 6.99,
        category: FoodCategory.salads,
        availableAddons: []
    ),

    //sides

    Food(
        name: 'French fries',
        description: 'Crispy, golden sticks of potato, salty and savory, perfect for dipping in your favorite condiment.',
        imagePath: 'lib/images/sides/m1.jpg',
        price: 9.99,
        category: FoodCategory.sides,
        availableAddons: [
          Addon(name: 'Extra ketchup', price: 5),
          Addon(name: 'Mayonnaise', price: 1.99),
        ]
    ),
    Food(
        name: 'Egg Whites',
        description: 'Clear, protein-rich liquid from eggs, often used in cooking and baking.',
        imagePath: 'lib/images/sides/m2.jpg',
        price: 7.99,
        category: FoodCategory.sides,
        availableAddons: [
          Addon(name: 'Extra lettuce', price: 5),
          Addon(name: 'Mayonnaise', price: 1.99),
        ]
    ),
    Food(
        name: 'Pepperoni Pizza',
        description: 'Savory, cheesy, and flavorful, a classic Italian dish with various toppings on a crispy crust',
        imagePath: 'lib/images/sides/m3.jpg',
        price: 8.99,
        category: FoodCategory.sides,
        availableAddons: [
          Addon(name: 'Extra pepperoni', price: 5),
          Addon(name: 'Mayonnaise', price: 1.99),
        ]
    ),
    Food(
        name: 'Cheese',
        description: 'Nutritious, melted or grated, a delicious addition to many dishes, with a rich, tangy flavor',
        imagePath: 'lib/images/sides/m4.jpg',
        price: 2.99,
        category: FoodCategory.sides,
        availableAddons: []
    ),
    Food(
        name: 'Lasagna',
        description: 'Layered pasta, meat sauce, and melted cheese, a classic Italian comfort food.',
        imagePath: 'lib/images/sides/m5.png',
        price: 9.99,
        category: FoodCategory.sides,
        availableAddons: [
          Addon(name: 'Extra ketchup', price: 5),
          Addon(name: 'Mayonnaise', price: 1.99),
        ]
    ),
  ];

  //Getters

// Operations

//Helpers
}