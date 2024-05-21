import 'package:delivr/models/food.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class AppFoodTile extends StatelessWidget {
  final Food food;
  final Function()? onTap;

  const AppFoodTile({
    super.key,
  required this.food,
  required this.onTap});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        GestureDetector(
          onTap: onTap,
          child: Padding(
            padding: const EdgeInsets.all(15.0),
            child: Row(
              children: [
                //Food Details
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(food.name),
                      Text('N ${food.price}', style: TextStyle(color: Theme.of(context).colorScheme.primary),),
                      Text(food.description)
                    ],
                  ),
                ),
                Image.asset(food.imagePath, height: 120, width: 120,fit: BoxFit.fill)
              ],
            ),
          ),
        )
      ],
    );
  }
}
