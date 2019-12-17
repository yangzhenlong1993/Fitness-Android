import 'package:flutter/material.dart';
import 'package:flutter_events/model/user_model.dart';

class MyPage extends StatefulWidget{
  final UserModel userModel;

  const MyPage({Key key, this.userModel}) : super(key: key);

  @override
  _MyPageState createState() => _MyPageState();
}

class _MyPageState extends State<MyPage>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Text('Profile'),
      ),
    );
  }
}