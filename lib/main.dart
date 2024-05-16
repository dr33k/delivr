import 'package:bloc/bloc.dart';
import 'package:delivr/auth/login_or_register.dart';
import 'package:delivr/screens/login_screen.dart';
import 'package:delivr/screens/register_screen.dart';
import 'package:delivr/simple_bloc_observer.dart';
import 'package:delivr/themes/theme_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  Bloc.observer = SimpleBlocObserver();

  runApp(ChangeNotifierProvider(
      create: (themeData) => ThemeProvider(),
      child: const MyApp()
  ));
}

class MyApp extends StatelessWidget{
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Delivr',
      debugShowCheckedModeBanner: false,
      home: const LoginOrRegister(),
      theme: Provider.of<ThemeProvider>(context).themeData,
    );
  }
}
