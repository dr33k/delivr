import 'dart:ffi';

import 'package:delivr/themes/dark_mode.dart';
import 'package:delivr/themes/light_mode.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ThemeProvider with ChangeNotifier{
  ThemeData _themeData = lightMode;

  ThemeData get themeData => _themeData;
  void set themeData(ThemeData td){
    _themeData = td;
    notifyListeners();
  }

  bool isDark() => _themeData == darkMode;

  void toggleTheme(){
    if(_themeData == lightMode) _themeData = darkMode;
    else _themeData = lightMode;
  }
}