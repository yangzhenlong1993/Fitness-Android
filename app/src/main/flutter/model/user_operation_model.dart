class UserOperationModel {
  final int errno;

  UserOperationModel({this.errno});

  factory UserOperationModel.fromJson(Map<String, dynamic> json) {
    return UserOperationModel(
      errno: json['errno'],
    );
  }
}
