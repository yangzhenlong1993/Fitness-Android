import 'package:flutter/material.dart';
import 'dart:async';

class AwaitPage extends StatefulWidget {
  final Future<int> requestCallback;

  AwaitPage(this.requestCallback);

  @override
  _AwaitPageState createState() => new _AwaitPageState();
}

class _AwaitPageState extends State<AwaitPage> {
  @override
  initState() {
    super.initState();
    new Timer(new Duration(seconds: 2), () {
      widget.requestCallback.then((int onValue) {
        Navigator.of(context).pop(onValue);
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Center(
      child: new CircularProgressIndicator(),
    );
  }
}
