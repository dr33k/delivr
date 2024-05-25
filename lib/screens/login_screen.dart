import 'package:delivr/components/app_button.dart';
import 'package:delivr/components/app_textfield.dart';
import 'package:delivr/screens/home_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class LoginScreen extends StatefulWidget {
  final Function()? onTap;

  const LoginScreen({super.key, required this.onTap});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  final TextEditingController emailController = TextEditingController();

  final TextEditingController passwordController = TextEditingController();

  void login(){
    /*
    * Authentication Logic here
    */
    //Navigate to homepage
    Navigator.push(context, MaterialPageRoute(builder: (context)=> const HomeScreen()));
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Theme.of(context).colorScheme.background,
        body: Center(
            child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            //logo
            Icon(Icons.lock_open_rounded,
                size: 100, color: Theme.of(context).colorScheme.inversePrimary),

            const SizedBox(height: 25),
            //message,slogan
            Text(
              "Krusty Krab",
              style: TextStyle(
                  fontSize: 16,
                  color: Theme.of(context).colorScheme.inversePrimary),
            ),

            const SizedBox(height: 25),

            //email textfield
            AppTextField(
                controller: emailController,
                hintText: "Email",
                obscureCharacter: false),

            const SizedBox(height: 25),

            //password textfield
            AppTextField(
                controller: passwordController,
                hintText: "Password",
                obscureCharacter: true),

            const SizedBox(height: 25),

            //signin button
            AppButton(onTap:login, text: "Sign In"),

            const SizedBox(height: 25),
            //not a member, login
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  "Not a member? ",
                  style:
                      TextStyle(color: Theme.of(context).colorScheme.inversePrimary),
                ),
                GestureDetector(
                  onTap: widget.onTap,
                  child: Text(
                    "Register now",
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.inversePrimary,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                )
              ],
            )
          ],
        )));
  }
}
