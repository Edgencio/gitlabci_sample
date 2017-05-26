Hi.view(function (_) {

    _.$preLoad = function () {
        _.tick_id = _.ticket_id;
        _.getTicketDetails();
        _.getTimelineInfo();
        _.timelineInfo = "";
    }

    _.$postLoad = function () {
        _.ticket = "";

    }

    _.back = function () {
        Hi.redirect("customer/tickets");
    }


    _.getTicketDetails = function () {
        CustomerTicketsFrontier.getTicketById(_.tick_id).try(function (result) {
            _.ticket = result;
            _.$apply();

        });
    }


    _.getTimelineInfo = function () {
        CustomerTicketsFrontier.getTicketInfo(_.tick_id).try(function (result) {
            _.timelineInfo = result;

            moment.locale('pt');
            for (a = 0; a < _.timelineInfo.length; a++) {
                var day = moment(_.timelineInfo[a].date);
                _.timelineInfo[a].date = moment(_.timelineInfo[a].date).format('LLL')
                // _.timelineInfo[a].date=moment().startOf(day).fromNow();
                console.log(_.timelineInfo[a].date);

            }
            _.$apply();
            console.log(_.timelineInfo);
        });
    }

});


