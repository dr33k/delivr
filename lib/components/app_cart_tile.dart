import 'package:delivr/components/app_quantity_selector.dart';
import 'package:delivr/models/cart_item.dart';
import 'package:delivr/models/food.dart';
import 'package:delivr/models/restaurant.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class AppCartTile extends StatelessWidget {
  final CartItem cartItem;
  
  const AppCartTile({super.key,
  required this.cartItem});

  @override
  Widget build(BuildContext context) {
    return Consumer<Restaurant>(builder: (context, restaurant, child)=> Container(
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.secondary,
        borderRadius: BorderRadius.circular(8)
      ),
      margin: const EdgeInsets.symmetric(horizontal: 25, vertical: 10),
      padding: const EdgeInsets.all(8.0),

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
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(cartItem.food.name, style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary)),
                    Text('N${cartItem.totalPrice.toString()}', style: TextStyle(color: Theme.of(context).colorScheme.primary)),
                   ],
                ),

                const Spacer(),

                AppQuantitySelector(quantity: cartItem.quantity,
                    food: cartItem.food,
                    onDecrement: (){ restaurant.removeFromCart(cartItem);},
                    onIncrement: (){ restaurant.addToCart(cartItem.food, cartItem.selectedAddons);}
                )
              ],
            ),
          // Addons
          SizedBox(
            height: cartItem.selectedAddons.isEmpty ? 0 : 60,
            child: ListView(
              scrollDirection: Axis.horizontal,
              children: cartItem.selectedAddons.map((addon)
              => Padding(
                padding: const EdgeInsets.only(right:10.0, bottom: 10.0, top: 10),
                child: FilterChip(
                    label: Row(
                      children: [
                        Text(addon.name),
                        const SizedBox(width: 5,),
                        Text('(N${addon.price.toString()})')
                      ],
                    ),
                    shape: StadiumBorder(
                      side: BorderSide(
                        color: Theme.of(context).colorScheme.inversePrimary
                      )
                    ),
                    backgroundColor: Theme.of(context).colorScheme.secondary,
                    labelStyle: TextStyle(
                      color: Theme.of(context).colorScheme.inversePrimary,
                      fontSize: 12
                    ),
                    onSelected: (value){}
                ),
              )).toList(),
            ),
          )
        ],
      ),
    )
    );
  }
}
