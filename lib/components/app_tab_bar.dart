import 'dart:ffi';

import 'package:delivr/models/food.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AppTabBar extends StatelessWidget {
  final TabController controller;

  const AppTabBar({super.key, required this.controller});

  List<Tab> _buildCategoryTabs(){
    return FoodCategory.values.map((c) {
      return Tab(
        text: c.toString().split('.').last,
      );
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: TabBar(
        controller: controller,
        tabs: _buildCategoryTabs()
      ),
    );
  }
}
