import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AppDrawerTile extends StatelessWidget {
  final String title;
  final IconData? icon;
  final Function()? onTap;

  const AppDrawerTile({super.key,
    required this.title,
    this.icon,
    this.onTap});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left: 8.0),
      child: ListTile(
          title: Text(title, style: TextStyle(color: Theme
              .of(context)
              .colorScheme
              .inversePrimary)),
          leading: Icon(icon, color: Theme
              .of(context)
              .colorScheme
              .inversePrimary),
          onTap: onTap
      ),
    );
  }
}
