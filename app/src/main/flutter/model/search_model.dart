import 'event_item_model.dart';

class SearchModel {
  String keyword;

  final int errno;
  final List<EventItemModel> data;

  SearchModel({this.errno, this.data});

  factory SearchModel.fromJson(Map<String, dynamic> json) {
    var dataJson = json['data'] as List;
    List<EventItemModel> data =
        dataJson.map((i) => EventItemModel.fromJson(i)).toList();
    return SearchModel(errno: json['errno'], data: data);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['errno'] = this.errno;
    data['data'] = this.data;

    return data;
  }
}
