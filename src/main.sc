require: timmetable.csv
  name = timetable
  var = timetable
  
require: slotfilling/slotFilling.sc
  module = sys.zb-common

theme: /

    state: Start
        q!: $regex</start>
        script:
            $context.session = {};
            $context.client = {};
            $context.temp = {};
            $context.response = {};
        a: Здравствуйте! Чем я могу вам помочь?

    state: Hello
        intent!: /Расписание
        if: 

    state: ChooseFilm
        a: Выберите фильм
        script:
            for (var id = 1; id < Object.keys(films).length + 1; id++) {
                var regions = pizza[id].value.region;
                if (_.contains(regions, $client.city)) {
                    var button_name = pizza[id].value.title;
                    $reactions.buttons({text: button_name, transition: 'GetName'})
                }
            }

    state: KnowledgeBase
        intentGroup!: /KnowledgeBase
        a: Нашёл ответ в базе знаний!
        script: $faq.pushReplies();

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}