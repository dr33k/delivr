import 'package:delivr/models/food.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AppQuantitySelector extends StatelessWidget {
  final int quantity;
  final Food food;
  final VoidCallback onIncrement;
  final VoidCallback onDecrement;

  const AppQuantitySelector({super.key,
  required this.quantity,
  required this.food,
  required this.onDecrement,
  required this.onIncrement});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.background,
        borderRadius: BorderRadius.circular(50)
      ),
      padding: const EdgeInsets.all(8),
      child: Row(
        children: [
          GestureDetector(
            onTap: onDecrement,
            child: Icon(Icons.remove,
            size: 20,
            color: Theme.of(context).colorScheme.primary,
            ),
          ),
          Padding(padding: const EdgeInsets.symmetric(horizontal: 8),
          child: SizedBox(
            width: 20,
            child: Center(
              child: Text(
                quantity.toString(),
                style: TextStyle(color: Theme.of(context).colorScheme.primary),
              ),
            ),
          ),
          ),
          GestureDetector(
            onTap: onIncrement,
            child: Icon(Icons.add,
              size: 20,
              color: Theme.of(context).colorScheme.primary,
            ),
          ),
        ],
      ),
    );
  }
}
