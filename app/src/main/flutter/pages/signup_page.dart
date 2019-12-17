import 'package:flutter/material.dart';
import 'package:flutter_events/dao/user_account_dao.dart';
import 'package:flutter_events/model/account_model.dart';
import 'package:flutter_events/model/user_model.dart';
import 'package:flutter_events/navigator/tab_navigator.dart';
import 'package:flutter_events/pages/await_page.dart';
import 'package:flutter_events/pages/prompt_page.dart';

class SignupPage extends StatefulWidget {
  @override
  _SignupPageState createState() => _SignupPageState();
}

class _SignupPageState extends State<SignupPage> {
  final TextEditingController _usernameController = new TextEditingController();
  final TextEditingController _passwordController = new TextEditingController();

  UserModel userModel;
  String _correctUsername = '';
  String _correctPassword = '';
  PromptPage promptPage = new PromptPage();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Stack(children: <Widget>[
      Opacity(
          opacity: 0.5,
          child: GestureDetector(
            onTap: () {
              FocusScope.of(context).requestFocus(new FocusNode());
            },
            child: Container(
              decoration: new BoxDecoration(
                image: new DecorationImage(
                  image: new ExactAssetImage('images/sign_up_background.jpg'),
                  fit: BoxFit.cover,
                ),
              ),
            ),
          )),
      Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Container(
            padding: EdgeInsets.only(left: 53, top: 140),
            width: MediaQuery.of(context).size.width * 0.77,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                TextField(
                    controller: _usernameController,
                    decoration: InputDecoration(
                      hintText: 'Username',
                      errorText:
                          (_correctUsername == "") ? null : _correctUsername,
                      icon: Icon(
                        Icons.account_circle,
                      ),
                    ),
                    onChanged: (String value) {
                      setState(() {
                        if (value.isEmpty) {
                          _correctUsername = "Username cannot be empty";
                        } else if (value.trim().length < 3) {
                          _correctUsername =
                              "Username length is less than 3 bits";
                        } else {
                          _correctUsername = "";
                        }
                      });
                    }),
                TextField(
                  controller: _passwordController,
                  obscureText: true,
                  keyboardType: TextInputType.number,
                  decoration: new InputDecoration(
                    hintText: 'Password',
                    errorText:
                        (_correctPassword == "") ? null : _correctPassword,
                    icon: new Icon(
                      Icons.lock_outline,
                    ),
                  ),
                  onChanged: (String value) {
                    setState(() {
                      if (value.isEmpty) {
                        _correctPassword = "Password cannot be empty";
                      } else if (value.trim().length < 6) {
                        _correctPassword =
                            "Password length is less than 6 bits";
                      } else {
                        _correctPassword = "";
                      }
                    });
                  },
                )
              ],
            ),
          ),
          Center(
            child: Container(
              padding: EdgeInsets.only(bottom: 60),
              child: FlatButton(
                child: Container(
                  height: 30,
                  width: 230,
                  decoration: BoxDecoration(
                    color: Theme.of(context).accentColor,
                  ),
                  child: Center(
                      child: Text("Join",
                          style: TextStyle(
                            color: Colors.white,
                          ))),
                ),
                onPressed: () {
                  print('Join');
                  String username = _usernameController.text;
                  String password = _passwordController.text;
                  _handleSubmit(username, password);
                },
              ),
            ),
          ),
        ],
      ),
      Container(
        padding: EdgeInsets.only(top: 45, left: 20),
        child: BackButton(),
      )
    ]));
  }

  _handleSubmit(String name, String password) async {
    FocusScope.of(context).requestFocus(new FocusNode());
    if (name == '' || password == '') {
      await promptPage.showMessage(context, "Invalid input!");
      return;
    }
    showDialog<int>(
        context: context,
        barrierDismissible: false,
        child: AwaitPage(_userSignup(
          name,
          password,
        ))).then((int onValue) async {
      if (onValue == 0) {
        _launchApp(userModel);
      } else if (onValue == 1) {
        await promptPage.showMessage(context, "Username already exists!");
      } else if (onValue == 2) {
        await promptPage.showMessage(
            context, "Username or password format is incorrect!");
      }
    });
  }

  Future<int> _userSignup(String name, String password) async {
    String signupArgs = 'signup?name=$name&password=$password';
    return await UserAccountDao.fetch(signupArgs).then((AccountModel model) async {
      if (model.errno == 0) {
        setState(() {
          userModel = model.data;
        });
        return 0;
      } else if (model.errno == 1) {
        return 1;
      } else if (model.errno == 2) {
        return 2;
      }
      ;
    });
  }

  _launchApp(UserModel model) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => TabNavigator(
                  userModel: model,
                )));
  }
}
