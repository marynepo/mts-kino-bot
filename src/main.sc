require: shows.csv
  name = shows
  var = shows
  
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


    state: /FilmTimetable
        intent!: /Расписание по фильму
        a: Нажмите на сеанс, если хотите купить билет.
        script:
            $session.film_id = $parseTree._film.film_id
            for (var id = 1; id < Object.keys(shows).length + 1; id++) {
                if ($session.film_id == shows[id].value.film_id) {
                    var button_name = shows[id].value.date + " в " + shows[id].value.time + " за " + shows[id].value.date;
                    $reactions.inlineButtons({text: button_name, callback_data: id })
                }
            }
            
        state: ClickButtons
            q: *
            a: Нажмите, пожалуйста, кнопку.
            go!: ..

    state: /DateTimetable
        intent!: /Расписание по дате
        a: Выберите сеанс
        script:
            $session.date = $parseTree._date.value.substring(1,10).replace("-",".");
            for (var id = 1; id < Object.keys(shows).length + 1; id++) {
                if ($session.date == shows[id].value.date) {
                    var button_name = shows[id].value.title + " в " + shows[id].value.time + " за " + shows[id].value.date;
                    $reactions.inlineButtons({text: button_name, callback_data: id })
                }
            }
            
    state: GetShowId
        event: telegramCallbackQuery
        script:
            $session.show_id = parseInt($request.query);
        a: здесь должен быть переход к шаблону с покупкой билетов

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}