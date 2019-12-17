import 'package:flutter/material.dart';

class DefaultPage extends StatefulWidget{
  @override
  _DefaultPageState createState() => _DefaultPageState();
}

class _DefaultPageState extends State<DefaultPage>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Text('Default'),
      ),
    );
  }
}