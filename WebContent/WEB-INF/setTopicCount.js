//修改用户足迹的topicCount
function setTopicCount() {
	var cursor = db.h_user_stat_shot.find();
	cursor.forEach(function(temp) {
		var arrayTopics = db.h_topic.find({
			"creatorId" : temp.accountId
		}).toArray();
//		print(temp._id);
//		print(arrayTopics.length);
		db.h_user_stat_shot.update({
			"_id" : temp._id
		}, {
			$set : {
				"topicCount" : arrayTopics.length
			}
		});
	})
}