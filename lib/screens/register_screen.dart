import 'package:delivr/components/app_button.dart';
import 'package:delivr/components/app_textfield.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class RegisterScreen extends StatefulWidget {
  final Function()? onTap;
  RegisterScreen({super.key, required this.onTap});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {

  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController()  ;
  final TextEditingController confirmPasswordController = TextEditingController();

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
                  "Let's create an account for you",
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

                //c password textfield
                AppTextField(
                    controller: confirmPasswordController,
                    hintText: "Confirm Password",
                    obscureCharacter: true),

                const SizedBox(height: 25),

                //signup button
                AppButton(onTap: () {}, text: "Sign Up"),

                const SizedBox(height: 25),
                //already have avvount, login
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Already have an account? ",
                      style:
                      TextStyle(color: Theme.of(context).colorScheme.inversePrimary),
                    ),
                    GestureDetector(
                      onTap: widget.onTap,
                      child: Text(
                        "Login now",
                        style: TextStyle(
                          color: Theme.of(context).colorScheme.inversePrimary,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    )
                  ],
                )
              ],
            )));  }
}
