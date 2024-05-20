import 'package:delivr/components/app_current_location.dart';
import 'package:delivr/components/app_delivery.dart';
import 'package:delivr/components/app_drawer.dart';
import 'package:delivr/components/app_sliver_app_bar.dart';
import 'package:delivr/components/app_tab_bar.dart';
import 'package:delivr/themes/theme_provider.dart';
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
    _tabController = TabController(length: 3, vsync: this);
  }
  @override
  void dispose(){
    _tabController.dispose();
    super.dispose();
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
        body: TabBarView(
          controller: _tabController,
          children: [
            ListView.builder(
                itemCount: 5,
                itemBuilder: (context, index)=> Text('Tab first items')
            ),ListView.builder(
                itemCount: 5,
                itemBuilder: (context, index)=> Text('Tab second items')
            ),ListView.builder(
                itemCount: 5,
                itemBuilder: (context, index)=> Text('Tab last items')
            ),


          ],
        ),
      ),
    );
  }
}
