import 'package:delivr/components/app_receipt.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class DeliveryProgressScreen extends StatelessWidget {
  const DeliveryProgressScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Delivery Progress"),
        backgroundColor: Colors.transparent,
        foregroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
bottomNavigationBar: _buildBottomNavigationBar(context),
      body: const Center(child: AppReceipt()),
    );
  }


  //Custom Button Navigation Bar

Widget _buildBottomNavigationBar(BuildContext context){
    return Container(
      padding: const EdgeInsets.all(25),
      height: 100,
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.secondary,
        borderRadius: const BorderRadius.only(topLeft: Radius.circular(40), topRight: Radius.circular(40)),
      ),
      child: Row(
        children: [
          Container(
           decoration: BoxDecoration(
             color: Theme.of(context).colorScheme.background,
              shape: BoxShape.circle
           ),
            child: IconButton(icon: const Icon(Icons.person),
            onPressed: (){},
            ),
          ),

          const SizedBox(width: 10),

          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text("Darwin Robinsosn", style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 18,
                color: Theme.of(context).colorScheme.inversePrimary,
              ),
              ),
              Text("Driver", style: TextStyle(
                color: Theme.of(context).colorScheme.primary,
              ),)
            ],
          ),
          const Spacer(),
          Row(
            children: [
              Container(
                decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.secondary,
                    shape: BoxShape.circle
                ),
                child: IconButton(icon: const Icon(Icons.message),
                  onPressed: (){},
                  color: Theme.of(context).colorScheme.primary,
                ),
              ),
              const SizedBox(width: 10,),
              Container(
                decoration: BoxDecoration(
                    color: Theme.of(context).colorScheme.secondary,
                    shape: BoxShape.circle
                ),
                child: IconButton(icon: const Icon(Icons.call),
                  onPressed: (){},
                  color: Colors.green,
                ),
              ),
            ],
          )
        ],
      ),
    );
}
}
