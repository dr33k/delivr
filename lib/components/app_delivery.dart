import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Delivery extends StatelessWidget {
  const Delivery({super.key});

  @override
  Widget build(BuildContext context) {

    var primaryTextStyle = TextStyle(color: Theme.of(context).colorScheme.inversePrimary, fontWeight: FontWeight.bold);
    var secondaryTextStyle = TextStyle(color: Theme.of(context).colorScheme.primary);

    return Container(
        decoration: BoxDecoration(
          border: Border.all(color: Theme.of(context).colorScheme.secondary),
          borderRadius: BorderRadius.circular(8)
        ),
        padding: const EdgeInsets.all(25.0),
        margin: const EdgeInsets.all(25),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Column(
              children: [
                Text('N2000.00', style: primaryTextStyle,),
                Text('Delivery fee', style: secondaryTextStyle,)
              ],
            ),
            Column(
              children: [
                Text('15 - 30 mins', style: primaryTextStyle),
                Text('Delivery time', style: secondaryTextStyle,)
              ],
            )
          ],
        ),
      );
  }
}
