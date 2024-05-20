import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AppSliverAppBar extends StatelessWidget {
  final Widget child;
  final Widget title;

  const AppSliverAppBar({super.key,
  required this.child,
  required this.title});


  @override
  Widget build(BuildContext context) {
    return SliverAppBar(
      backgroundColor:Theme.of(context).colorScheme.background,
      foregroundColor:Theme.of(context).colorScheme.inversePrimary,
      title: const Text('Mae\'s Delight'),
      flexibleSpace: FlexibleSpaceBar(
          background: Padding(
            padding: const EdgeInsets.only(bottom: 20.0),
            child: child,
          ),
          title: title,
          centerTitle: true,
          titlePadding: const EdgeInsets.only(left: 0, right: 0, top: 0),
        expandedTitleScale: 1,
      ),
      expandedHeight: 320,
      collapsedHeight: 120,
      pinned: true,
      floating: false,

      actions: [
        IconButton(onPressed: (){},
            icon: const Icon(Icons.shopping_cart))
      ]
    );
  }
}
