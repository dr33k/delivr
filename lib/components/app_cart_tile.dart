import 'package:delivr/models/cart_item.dart';
import 'package:delivr/models/food.dart';
import 'package:delivr/models/restaurant.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';

class AppCartTile extends StatelessWidget {
  final CartItem cartItem;
  
  const AppCartTile({super.key,
  required this.cartItem});

  @override
  Widget build(BuildContext context) {
    return Consumer<Restaurant>(builder: (context, restaurant, child)=> Container(

      child: Column(
        children: [
          Row(
            children: [
              //Food image
              ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: Image.asset(cartItem.food.imagePath, height: 100, width: 100, fit: BoxFit.fill,)
              ),
              const SizedBox(width: 10),

              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(cartItem.food.name, style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary)),
                  Text('N${cartItem.totalPrice.toString()}', style: TextStyle(color: Theme.of(context).colorScheme.primary),)
                ],
              )
            ],
          )
        ],
      ),
    )
    );
  }
}
