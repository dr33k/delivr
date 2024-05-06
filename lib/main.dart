import 'package:bloc/bloc.dart';
import 'package:delivr/screens/login_screen.dart';
import 'package:delivr/simple_bloc_observer.dart';
import 'package:flutter/material.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  Bloc.observer = SimpleBlocObserver();

  runApp(const MyApp());
}

class MyApp extends StatelessWidget{
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Delivr',
      debugShowCheckedModeBanner: false,
      home: LoginScreen()
    );
  }
}
