import 'package:delivr/components/app_receipt.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

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

      body: const Center(child: AppReceipt()),
    );
  }
}
