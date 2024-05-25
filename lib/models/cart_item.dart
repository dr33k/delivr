import 'package:delivr/models/food.dart';
import 'package:flutter/cupertino.dart';

class CartItem {
  Food food;
  List<Addon> selectedAddons;
  int quantity;

  CartItem({
 required this.food,
    required this.selectedAddons,
}): quantity = 1;

  double get totalPrice{
    double addonsTotal = selectedAddons.fold(0, (sum, addon) => sum + addon.price);
    return (food.price + addonsTotal) * quantity;
  }


}
