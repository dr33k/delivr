import 'package:delivr/components/app_button.dart';
import 'package:delivr/models/food.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/widgets.dart';

class FoodScreen extends StatefulWidget {
  final Food food;
  
  const FoodScreen({
    super.key,
  required this.food});

  @override
  State<FoodScreen> createState() => _FoodScreenState();
}

class _FoodScreenState extends State<FoodScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: Column(
                children: [
                  //food image
                  ClipRRect(child: Image.asset(widget.food.imagePath, height: 500, width: 500, fit: BoxFit.fill,)),
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
                                        value: false,
                                        onChanged: (value){}
                                    );
                              }),
                            ),
                          AppButton(onTap: (){}, text: 'Add to Cart'),

                          const SizedBox(height: 30),
                        ],
                      ),
                    ),
        
                  //button -> add to cart
          ],
        ),
      ),
    );
  }
}
