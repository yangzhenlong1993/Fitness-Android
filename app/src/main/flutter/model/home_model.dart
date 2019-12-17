import 'package:flutter_events/model/event_item_model.dart';

class HomeModel {
  final List<EventItemModel> data;

  HomeModel({
    this.data,
  });

  factory HomeModel.fromJson(Map<String, dynamic> json) {
    var dataJson = json['data'] as List;
    List<EventItemModel> data =
        dataJson.map((i) => EventItemModel.fromJson(i)).toList();

    return HomeModel(
      data: data,
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['data'] = this.data;

    return data;
  }
}
