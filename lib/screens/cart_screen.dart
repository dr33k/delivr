import 'package:delivr/components/app_cart_tile.dart';
import 'package:delivr/models/cart_item.dart';
import 'package:delivr/models/restaurant.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class CartScreen extends StatelessWidget {
  const CartScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<Restaurant>(builder: (context, restaurant, child){
      List<CartItem> cart = restaurant.cart;

      return Scaffold(
        appBar: AppBar(
          title: const Text("Cart"),
          backgroundColor: Colors.transparent,
          foregroundColor: Theme.of(context).colorScheme.inversePrimary,
        ),
          body: Column(
            children: [
              Expanded(child: ListView.builder(
                  itemCount: cart.length,
                  itemBuilder: (context, index)=> AppCartTile(cartItem: cart[index])
              ))
            ],
          )
      );
    });
  }
}
