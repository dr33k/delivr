import 'package:delivr/components/app_button.dart';
import 'package:delivr/models/food.dart';
import 'package:delivr/models/restaurant.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';

class FoodScreen extends StatefulWidget {
  final Food food;
  final Map<Addon, bool> selectedAddons = {};

  FoodScreen({
    super.key,
  required this.food}){
  for(Addon addon in food.availableAddons){
    selectedAddons[addon] = false;
  }
  }

  @override
  State<FoodScreen> createState() => _FoodScreenState();
}

class _FoodScreenState extends State<FoodScreen> {

  void addToCart(Food food, Map<Addon, bool> selectedAddons){
    context.read<Restaurant>().addToCart(food, selectedAddons.keys.where((key) => selectedAddons[key]!).toList());
    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return 
      Stack(
        children:[
          Scaffold(
             body: SingleChildScrollView(
              child: Column(
                  children: [
                    //food image
                    ClipRRect(child: Image.asset(widget.food.imagePath, height: 450, width: 500, fit: BoxFit.fill,)),
                    //column
                    Padding(
                        padding: const EdgeInsets.all(25.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text(widget.food.name, style: TextStyle(
                                        color: Theme.of(context).colorScheme.inversePrimary,
                                        fontWeight: FontWeight.bold,
                                        fontSize: 20
                                    ),
                                    ),
          
                                const SizedBox(width: 20,),
          
                                Text('N${widget.food.price.toString()}', style: TextStyle(
                                    color: Theme.of(context).colorScheme.inversePrimary,
                                    fontWeight: FontWeight.bold,
                                    fontSize: 20
                                ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 10),
        
                            //food description
                            Text(widget.food.description, style: TextStyle(
                                color: Theme.of(context).colorScheme.primary,
                                fontSize: 16
                            ),),
          
                            const SizedBox(height: 30),
          
                            Text('Add-ons', style: TextStyle(
                                color: Theme.of(context).colorScheme.inversePrimary,
                                fontSize: 16
                            ),
                            ),
        
                            //addons
                            Container(
                              decoration: BoxDecoration(
                                border: Border.all(color: Theme.of(context).colorScheme.secondary,),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              padding: const EdgeInsets.only(top: 0, bottom: 25.0, left: 25.0, right: 25.0),
                              margin: const EdgeInsets.symmetric(vertical: 20),
                              child: ListView.builder(
                                    itemCount: widget.food.availableAddons.length,
                                    shrinkWrap: true,
                                    physics: const NeverScrollableScrollPhysics(),
                                    itemBuilder: (context, index){
                                  Addon addon = widget.food.availableAddons[index];
        
                                  return
                                      CheckboxListTile(
                                          title: Text(addon.name, style: TextStyle(
                                              color: Theme.of(context).colorScheme.inversePrimary,
                                              fontSize: 16
                                          ),),
                                          subtitle: Text('N${addon.price.toString()}',
                                            style: TextStyle(
                                              color: Theme.of(context).colorScheme.primary,
                                              fontSize: 16
                                          ),
                                          ),
                                          value: widget.selectedAddons[addon],
                                          onChanged: (bool? value){
                                            setState((){
                                              widget.selectedAddons[addon] = value!;
                                            });
                                          }
                                      );
                                }),
                              ),

                            //button -> add to cart
                            AppButton(onTap: ()=> addToCart( widget.food,  widget.selectedAddons), text: 'Add to Cart'),
        
                            const SizedBox(height: 30),
                          ],
                        ),
                      ),
                  ],
          ),
        ),
          ),
          
          //back button
          SafeArea(
            child: Opacity(
              opacity: 0.6,
              child: Container(
                margin: const EdgeInsets.only(left: 25),
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: Theme.of(context).colorScheme.secondary
                ),
                child: IconButton(
                  icon: const Icon(Icons.arrow_back_rounded),
                  onPressed: ()=> Navigator.pop(context),
                  color: Theme.of(context).colorScheme.inversePrimary,
                ),
              ),
            ),
          )
    ]
      );
  }
}
