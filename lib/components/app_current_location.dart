import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class AppCurrentLocation extends StatelessWidget {
  const AppCurrentLocation({super.key});

  void openLocationSearchBox(BuildContext context){
    showDialog(context: context,
        builder: (context)=> AlertDialog(
          title: const Text('Your Location'),
          content: const TextField(
            decoration:  InputDecoration(hintText: 'Search Address'),
          ),
          actions: [
            MaterialButton(
              child: const Text('Cancel'),
              onPressed: () => Navigator.pop(context),
            ),
            MaterialButton(
              child: const Text('Save'),
              onPressed: () => Navigator.pop(context),
            ),
          ],
        ));
  }
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(25.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text('Deliver Now',
          style: TextStyle(color: Theme.of(context).colorScheme.primary)
          ),
          GestureDetector(
            onTap: ()=>openLocationSearchBox(context),
            child: Row(
              children: [
                Text('Wuse Zone 1',
                style: TextStyle(
                  color: Theme.of(context).colorScheme.inversePrimary,
                  fontWeight: FontWeight.bold
                ),),
                Icon(Icons.keyboard_arrow_down_rounded)
              ],
            ),
          )
        ],
      ),
    );
  }
}
