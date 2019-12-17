import 'package:flutter/material.dart';
import 'package:flutter_events/dao/search_dao.dart';
import 'package:flutter_events/model/search_model.dart';
import 'package:flutter_events/model/user_model.dart';
import 'package:flutter_events/widget/event_list.dart';

const ARGS = 'events?type=';

class EventsPage extends StatefulWidget {
  final UserModel userModel;
  final String args;
  final String type;
  final String title;
  final bool hideIcon;

  const EventsPage(
      {Key key,
      this.args = ARGS,
      this.type,
      this.hideIcon = false,
      this.title,
      this.userModel})
      : super(key: key);

  @override
  _EventsPageState createState() => _EventsPageState();
}

class _EventsPageState extends State<EventsPage> {
  SearchModel searchModel;

  @override
  void initState() {
    super.initState();
    _fetchData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: _body(context),
      ),
    );
  }

  _fetchData() {
//    if (widget.type.length == 0) {
//      setState(() {
//        searchModel = null;
//      });
//      return;
//    }

    String args = widget.args;

    if (widget.type != null) {
      args = widget.args + widget.type;
    }
    SearchDao.fetch(args, '').then((SearchModel model) {
      setState(() {
        searchModel = model;
      });
    }).catchError((e) {});
  }

  _appBar(Color backgroundColor, Color backButtonColor, String title) {
    return Container(
      height: 70,
      color: backgroundColor,
      child: FractionallySizedBox(
        widthFactor: 1,
        child: Stack(
          children: <Widget>[
            //Return button
            GestureDetector(
              onTap: () {
                Navigator.pop(context);
              },
              child: Container(
                margin: EdgeInsets.fromLTRB(8, 35, 10, 5),
                child: Icon(
                  Icons.close,
                  color: backButtonColor,
                  size: 26,
                ),
              ),
            ),
            // Title
            Positioned(
              left: 0,
              right: 0,
              top: 35,
              child: Center(
                child: Text(
                  title,
                  style: TextStyle(color: backButtonColor, fontSize: 22),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  _body(BuildContext context) {
    return Column(
      children: <Widget>[
        _appBar(Colors.red, Colors.white, widget.title),
        MediaQuery.removePadding(
            context: context,
            removeTop: true,
            child: Expanded(
                flex: 1,
                child: EventList(
                  searchModel: this.searchModel,
                  userModel: widget.userModel,
                ))),
      ],
    );
  }
}
