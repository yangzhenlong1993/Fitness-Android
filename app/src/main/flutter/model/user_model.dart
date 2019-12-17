import 'event_item_model.dart';

class UserModel {
  final int userId;
  final String name;
  final String password;
  final int level;

  final List<EventItemModel> joinedEvents;

  UserModel({
    this.userId,
    this.name,
    this.password,
    this.level,
    this.joinedEvents,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    var joinedEventsJson = json['joinedEvents'] as List;
    List<EventItemModel> joinedEvents = [];
    if (joinedEventsJson != null) {
      joinedEvents =
          joinedEventsJson.map((i) => EventItemModel.fromJson(i)).toList();
    }

    return UserModel(
      userId: json['userId'],
      name: json['name'],
      password: json['password'],
      level: json['level'],
      joinedEvents: joinedEvents,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['userId'] = this.userId;
    data['name'] = this.name;
    data['password'] = this.password;
    data['level'] = this.level;
    data['joinedEvents'] = this.joinedEvents;

    return data;
  }
}
