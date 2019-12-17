import 'package:flutter/material.dart';

class PromptPage {
  showMessage(BuildContext context, String text) {
    showDialog<Null>(
        context: context,
        child: AlertDialog(
            title: Text("EVENT"),
            content: Text(text),
            actions: <Widget>[
              FlatButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text('OK'))
            ]));
  }
}
