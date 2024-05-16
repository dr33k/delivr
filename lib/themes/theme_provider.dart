import 'dart:ffi';

import 'package:delivr/themes/dark_mode.dart';
import 'package:delivr/themes/light_mode.dart';
import 'package:flutter/material.dart';

class ThemeProvider with ChangeNotifier{
  ThemeData _themeData = lightMode;

  ThemeData get themeData => _themeData;
  set themeData(ThemeData td){
    _themeData = td;
    notifyListeners();
  }

  bool get isDark => _themeData == darkMode;

  void toggleTheme(){
    if(_themeData == lightMode){ themeData = darkMode;}
    else {themeData = lightMode;}
  }
}