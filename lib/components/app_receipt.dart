import 'package:delivr/models/restaurant.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class AppReceipt extends StatelessWidget {
  const AppReceipt({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(25),
    child: Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Text("Thank you, your order will arrive soon.",
              style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),
            ),
            const SizedBox(height: 25),
            Container(
              decoration: BoxDecoration(
                border: Border.all(color: Theme.of(context).colorScheme.secondary),
                borderRadius: BorderRadius.circular(8)
              ),
              padding: const EdgeInsets.all(25),
              child: Consumer<Restaurant>(
                builder: (context, restaurant, child)=>
                Text(restaurant.displayCardReceipt()),
              ),
            )
        ],
      ),
    ),);
  }
}
