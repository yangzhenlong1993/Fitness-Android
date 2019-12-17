import 'package:flutter/material.dart';
import 'package:flutter_events/model/user_model.dart';
import 'package:flutter_events/pages/events_page.dart';

const TYPES = ['Ball', 'Bike', 'Exercise', 'Running', 'Swim', 'Yoga'];

class GridNav extends StatelessWidget {
  final UserModel userModel;

  const GridNav({Key key, this.userModel}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(left: 3),
      child: _gridNavItems(context),
    );
  }

  _wrapGesture(BuildContext context, Widget widget, String title) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => EventsPage(
                      type: title.toLowerCase(),
                      title: title,
                      userModel: this.userModel,
                    )));
      },
      child: widget,
    );
  }

  _gridNavItems(BuildContext context) {
    return Column(
      children: <Widget>[_firstLine(context), _secondLine(context)],
    );
  }

  _firstLine(BuildContext context) {
    Widget mainItem = _mainItem(context);
    Widget doubleItems = _doubleItems(context, 'Exercise', 'Running');
    return Row(
      children: <Widget>[
        mainItem,
        Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
                begin: Alignment.topRight,
                end: Alignment.bottomLeft,
                colors: [Color(0xffbe0027), Color(0xffff0000)]),
          ),
          child: doubleItems,
        ),
      ],
    );
  }

  _secondLine(BuildContext context) {
    Widget firstDouble = _doubleItems(context, 'Ball Sports', 'Swim');
    Widget secondDouble = _doubleItems(context, 'Bike', 'Yoga / Pilates');
    return Container(
      padding: EdgeInsets.only(top: 3),
      child: Row(
        children: <Widget>[
          Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                  begin: Alignment.bottomLeft,
                  end: Alignment.topRight,
                  colors: [Color(0xffbe0027), Color(0xffff0000)]),
            ),
            child: firstDouble,
          ),
          Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                  begin: Alignment.bottomRight,
                  end: Alignment.topLeft,
                  colors: [Color(0xffbe0027), Color(0xffff0000)]),
            ),
            child: secondDouble,
          ),
        ],
      ),
    );
  }

  _doubleItems(BuildContext context, String top, String bottom) {
    Widget topItem = _item(context, top, true);
    Widget bottomItem = _item(context, bottom, false);
    return Column(
      children: <Widget>[topItem, bottomItem],
    );
  }

  _mainItem(BuildContext context) {
    BorderSide borderSide = BorderSide(width: 3, color: Color(0xfff2f2f2));
    return _wrapGesture(
        context,
        Container(
          height: 200,
          width: 204,
          alignment: Alignment.bottomLeft,
          decoration: BoxDecoration(
            border: Border(right: borderSide),
            gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [Color(0xffbe0027), Color(0xffff0000)]),
          ),
          child: Container(
            padding: EdgeInsets.fromLTRB(20, 0, 0, 20),
            child: Text(
              'All',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 35, color: Colors.white),
            ),
          ),
        ),
        'All');
  }

  _item(BuildContext context, String title, bool isFirst) {
    BorderSide borderSide = BorderSide(width: 3, color: Color(0xfff2f2f2));
    return _wrapGesture(
        context,
        Container(
            height: 100,
            width: 204,
            alignment: Alignment.bottomLeft,
            decoration: BoxDecoration(
              border: Border(
                  right: borderSide,
                  bottom: isFirst ? borderSide : BorderSide.none),
            ),
            child: Container(
              padding: EdgeInsets.fromLTRB(20, 0, 0, 20),
              child: Text(
                title,
                textAlign: TextAlign.center,
                style: TextStyle(fontSize: 20, color: Colors.white),
                softWrap: true,
              ),
            )),
        title);
  }
}
