import 'dart:core';

import 'package:delivr/components/app_current_location.dart';
import 'package:delivr/components/app_delivery.dart';
import 'package:delivr/components/app_drawer.dart';
import 'package:delivr/components/app_food_tile.dart';
import 'package:delivr/components/app_sliver_app_bar.dart';
import 'package:delivr/components/app_tab_bar.dart';
import 'package:delivr/models/food.dart';
import 'package:delivr/models/restaurant.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatefulWidget {

  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState(){
    super.initState();
    _tabController = TabController(length: FoodCategory.values.length, vsync: this);
  }
  @override
  void dispose(){
    _tabController.dispose();
    super.dispose();
  }

  List<Food> _filterMenuByCategory(FoodCategory category, List<Food> fullMenu){
    return fullMenu.where((element) => element.category == category).toList();
  }

  List<Widget> _sortFood(List<Food> fullMenu){
    return FoodCategory.values.map((category) {
      List<Food> categories = _filterMenuByCategory(category, fullMenu);

      return ListView.builder(
          itemCount: categories.length,
          physics: const NeverScrollableScrollPhysics(),
          padding: EdgeInsets.zero,
          itemBuilder: (context, index){
        return AppFoodTile(food: categories[index], onTap: (){});
      });
    }).toList();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      drawer: AppDrawer(),

      body: NestedScrollView(
        headerSliverBuilder: (context, innerBoxIsScrolled)=>
        [
          AppSliverAppBar(
            title: AppTabBar(controller: _tabController),

            child:Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                //My Current Location
                Divider(
                  indent: 25,
                  endIndent: 25,
                  color: Theme.of(context).colorScheme.secondary,
                ),
                AppCurrentLocation(),

                Delivery()
              ],
            ))],
        body: Consumer<Restaurant>(
          builder: (context, restaurant, child) => TabBarView(
              controller: _tabController,
              children: _sortFood(restaurant.menu)
          ),
        )
      ),
    );
  }
}
