import 'package:delivr/components/app_button.dart';
import 'package:delivr/screens/delivery_progress_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_credit_card/flutter_credit_card.dart';

class PaymentScreen extends StatefulWidget {
  const PaymentScreen({super.key});

  @override
  State<PaymentScreen> createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {

  GlobalKey<FormState> formKey = GlobalKey<FormState>();
  String cardNumber = '';
  String cardHolderName = '';
  String cvvCode = '';
  String expiryDate = '';
  bool isCvvFocused = false;

  void onPay(){
    //Only show dialog is valid
    if(formKey.currentState!.validate()){
      showDialog(context: context,
          builder: (context) => AlertDialog(
            title: const Text("Confirm payment"),
            titleTextStyle: TextStyle(
                color: Theme.of(context).colorScheme.inversePrimary,
                fontWeight: FontWeight.bold,
              fontSize: 20
            ),

            content: SingleChildScrollView(
              child: ListBody(
                children: [
                  Text("Card Number $cardNumber"),
                  Text("Expiry Date $expiryDate"),
                  Text("Card Holder Name $cardHolderName"),
                  Text("CVV Code $cvvCode"),
                ],
              ),
            ),
            contentTextStyle: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),
            actions: [
              TextButton(
                  child: Text("Cancel", style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary)),
                  onPressed: ()=> Navigator.pop(context)
              ),

              TextButton(
                  child: Text("Confirm", style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),),
                  onPressed: () {
                    Navigator.pop(context);
                    Navigator.push(context, MaterialPageRoute(
                        builder: (context) => DeliveryProgressScreen()
                    ));
                  }
              ),

            ],
          ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Checkout",
          style: TextStyle(color: Theme.of(context).colorScheme.inversePrimary),),
        backgroundColor: Colors.transparent,
        foregroundColor: Theme.of(context).colorScheme.inversePrimary,

      ),

      body: ListView(
        children: [
          //Credit card widget

          CreditCardWidget(
              cardNumber: cardNumber,
              expiryDate: expiryDate,
              cardHolderName: cardHolderName,
              cvvCode: cvvCode,
              showBackView: isCvvFocused,
              onCreditCardWidgetChange: (value){},
            cardBgColor: Theme.of(context).colorScheme.tertiary,
            textStyle: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 18,
                color: Theme.of(context).colorScheme.inversePrimary),
          ),

          CreditCardForm(
              cardNumber: cardNumber,
              expiryDate: expiryDate,
              cardHolderName: cardHolderName,
              cvvCode: cvvCode,
              formKey: formKey,
              onCreditCardModelChange: (data){
                setState(() {
                  cardNumber = data.cardNumber;
                  expiryDate = data.expiryDate;
                  cardHolderName = data.cardHolderName;
                  cvvCode = data.cvvCode;
                  isCvvFocused = data.isCvvFocused;
                });
              },

          ),

          const SizedBox(height: 30),

          AppButton(
              onTap: onPay,
              text: "Pay"
          ),

          const SizedBox(height: 10)

        ],
      ),
    );
  }
}
