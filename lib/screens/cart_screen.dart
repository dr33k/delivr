import 'package:delivr/components/app_button.dart';
import 'package:delivr/components/app_cart_tile.dart';
import 'package:delivr/models/cart_item.dart';
import 'package:delivr/models/restaurant.dart';
import 'package:delivr/screens/payment_screen.dart';
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
          actions: [
            IconButton(
                onPressed: (){
                  showDialog(
                  context: context,
                  builder: (context) => AlertDialog(
                    title: Text("Are you sure you want to clear the cart ?",  style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),),
                    actions: [
                      TextButton(
                          onPressed: ()=> Navigator.pop(context),
                          child: Text("Cancel", style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),),
                      ),
                      TextButton(
                          onPressed: (){
                            restaurant.clearCart();
                            Navigator.pop(context);
                          },
                          child: Text("Yes",  style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),),
                      )
                    ],
                  )
                );},
                icon: const Icon(Icons.delete))
          ],
        ),
          body: Column(
            children: [

              //Cart List
              Expanded(
                child: Column(
                  children: [
                    cart.isEmpty?
                        Expanded(
                          child: Center(
                              child: Text("There's nothing here ...",
                                style: TextStyle(
                                    fontSize: 20,
                                    color: Theme.of(context).colorScheme.primary),
                              )
                          ),
                        ):
                    Expanded(child: ListView.builder(
                        itemCount: cart.length,
                        itemBuilder: (context, index)=> AppCartTile(cartItem: cart[index])
                    ))
                  ],
                ),
              ),

              //Pay button
              AppButton(
                  onTap: ()=> Navigator.push(context, MaterialPageRoute(builder: (context)=> PaymentScreen())),
                  text: "Checkout (N${restaurant.getTotalPrice().toStringAsFixed(2)})"),

              const SizedBox(height: 25),
            ],
          )
      );
    });
  }
}
