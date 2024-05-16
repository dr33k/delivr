import 'package:delivr/components/app_drawer_tile.dart';
import 'package:delivr/screens/settings_screen.dart';
import 'package:flutter/material.dart';
class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      backgroundColor: Theme.of(context).colorScheme.background,
      child: Column(
        children: [
        //app logo
        Padding(
          padding: const EdgeInsets.only(top: 100.0),
          child: Icon(
              Icons.lock_open_rounded,
          size: 80,
          color: Theme.of(context).colorScheme.inversePrimary,
          ),
        ),

        Padding(padding: EdgeInsets.all(30),
        child: Divider(
          color: Theme.of(context).colorScheme.inversePrimary,
        ),),
        //home list title
        AppDrawerTile(title:"H O M E",
        icon: Icons.home,
        onTap: ()=> Navigator.pop(context),),

          AppDrawerTile(title:"S E T T I N G S",
        icon: Icons.settings,
        onTap: () {
            Navigator.pop(context);
            Navigator.push(context,
                MaterialPageRoute(builder: (context)=> const SettingsScreen()));

        },),

          const Spacer(),

          AppDrawerTile(title:"L O G O U T",
        icon: Icons.logout ,
        onTap: () {},),
        //settings list title

        //logout list title
      ]
      ),
    );
  }
}
