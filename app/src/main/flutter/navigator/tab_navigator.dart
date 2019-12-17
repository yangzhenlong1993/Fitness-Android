import 'package:flutter/material.dart';
import 'package:flutter_events/model/user_model.dart';
import 'package:flutter_events/pages/add_event_page.dart';
import 'package:flutter_events/pages/discover_page.dart';
import 'package:flutter_events/pages/events_page.dart';
import 'package:flutter_events/pages/home_page.dart';
import 'package:flutter_events/pages/my_page.dart';
import 'package:flutter_events/pages/search_page.dart';

class TabNavigator extends StatefulWidget {
  final UserModel userModel;

  const TabNavigator({Key key, this.userModel}) : super(key: key);

  @override
  _TabNavigatorState createState() => _TabNavigatorState();
}

class _TabNavigatorState extends State<TabNavigator> {
  final _defaultColor = Colors.grey;
  final _activeColor = Colors.black;
  int _currentIndex = 0;
  final PageController _controller = PageController(
    initialPage: 0,
  );

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PageView(
        physics: NeverScrollableScrollPhysics(),
        controller: _controller,
        children: <Widget>[
          HomePage(
            userModel: widget.userModel,
          ),
          SearchPage(
            userModel: widget.userModel,
            hideLeft: true,
          ),
          AddEventPage(
            userModel: widget.userModel,
          ),
          MyPage(
            userModel: widget.userModel,
          )
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
          currentIndex: _currentIndex,
          onTap: (index) {
            _controller.jumpToPage(index);
            setState(() {
              _currentIndex = index;
            });
          },
          items: [
            BottomNavigationBarItem(
                icon: Icon(Icons.home, color: _defaultColor),
                activeIcon: Icon(Icons.home, color: _activeColor),
                title: Text('Home',
                    style: TextStyle(
                        fontSize: 16,
                        color: _currentIndex != 0
                            ? _defaultColor
                            : _activeColor))),
            BottomNavigationBarItem(
                icon: Icon(Icons.search, color: _defaultColor),
                activeIcon: Icon(Icons.search, color: _activeColor),
                title: Text('Search',
                    style: TextStyle(
                        fontSize: 16,
                        color: _currentIndex != 1
                            ? _defaultColor
                            : _activeColor))),
            BottomNavigationBarItem(
                icon: Icon(Icons.add_box, color: _defaultColor),
                activeIcon: Icon(Icons.add_box, color: _activeColor),
                title: Text('Add',
                    style: TextStyle(
                        fontSize: 16,
                        color: _currentIndex != 2
                            ? _defaultColor
                            : _activeColor))),
            BottomNavigationBarItem(
                icon: Icon(Icons.person, color: _defaultColor),
                activeIcon: Icon(Icons.person, color: _activeColor),
                title: Text('Profile',
                    style: TextStyle(
                        fontSize: 16,
                        color:
                            _currentIndex != 3 ? _defaultColor : _activeColor)))
          ]),
    );
  }
}
