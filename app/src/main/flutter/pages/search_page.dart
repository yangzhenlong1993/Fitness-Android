import 'package:flutter/material.dart';
import 'package:flutter_events/dao/search_dao.dart';
import 'package:flutter_events/model/search_model.dart';
import 'package:flutter_events/model/user_model.dart';
import 'package:flutter_events/widget/event_list.dart';
import 'package:flutter_events/widget/search_bar.dart';

const URL = 'http://10.0.2.2:5000/';
const TYPES = ['ball', 'bike', 'exercise', 'running', 'swim', 'yoga'];

class SearchPage extends StatefulWidget {
  final UserModel userModel;
  final bool hideLeft;
  final String keyword;
  final String hint;

  const SearchPage(
      {Key key,
      this.hideLeft,
      this.keyword,
      this.hint,
      this.userModel})
      : super(key: key);

  @override
  _SearchPageState createState() => _SearchPageState();
}

class _SearchPageState extends State<SearchPage> {
  SearchModel searchModel;
  String keyword;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: <Widget>[
          _appBar(),
          MediaQuery.removePadding(
              context: context,
              removeTop: true,
              child: Expanded(
                  flex: 1,
                  child: EventList(
                    searchModel: searchModel,
                    userModel: widget.userModel,
                  ))),
        ],
      ),
    );
  }

  _appBar() {
    return Column(
      children: <Widget>[
        Container(
          decoration: BoxDecoration(
              gradient: LinearGradient(
                  colors: [Color(0x66000000), Colors.transparent],
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter)),
          child: Container(
            padding: EdgeInsets.only(top: 20),
            height: 75,
            decoration: BoxDecoration(color: Colors.white),
            child: SearchBar(
              hideLeft: widget.hideLeft,
              defaultText: widget.keyword,
              hint: 'Try running, exercise',
              leftButtonClick: () {
                Navigator.pop(context);
              },
              rightButtonClick: () {
                //hide keyboard
                FocusScope.of(context).requestFocus(FocusNode());
              },
              onChanged: _onTextChange,
            ),
          ),
        )
      ],
    );
  }

  _onTextChange(text) {
    keyword = text;
    if (text.length == 0) {
      setState(() {
        searchModel = null;
      });
      return;
    }
    String searchArgs = 'search?keyword=$text';
    SearchDao.fetch(searchArgs, text).then((SearchModel model) {
      if (model.keyword == keyword) {
        setState(() {
          searchModel = model;
        });
      }
    }).catchError((e) {});
  }
}
