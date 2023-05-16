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


    state: Timetable
        intent!: /Расписание
        script:
            var n_buttons = 0;
            if ($parseTree._film) {
                $reactions.answer("Нажмите на сеанс, если хотите узнать подробную информацию или купить билет.")
                $session.film_id = $parseTree._film.film_id
                if ($parseTree._date) {
                    $session.date = $parseTree._date.value.substring(0,10);
                    $session.date = $session.date.substring(8,10) + "." + $session.date.substring(5,7) + "." + $session.date.substring(0,4)
                    for (var id = 1; id < Object.keys(shows).length + 1; id++) {
                        if (($session.date == shows[id].value.date ) && ($session.film_id == shows[id].value.film_id)) {
                            var button_name = shows[id].value.time;
                            n_buttons = n_buttons + 1;
                            $reactions.inlineButtons({text: button_name, callback_data: id})
                        }
                    }
                }
            
            
                else {
                    for (var id = 1; id < Object.keys(shows).length + 1; id++) {
                        if ($session.film_id == shows[id].value.film_id) {
                            var button_name = shows[id].value.date.substring(0,6) + " в " + shows[id].value.time;
                            n_buttons = n_buttons + 1;
                            $reactions.inlineButtons({text: button_name, callback_data: id})
                        }
                    }
                }
            }
            
            else if ($parseTree._date) {
                $reactions.answer("Нажмите на сеанс, если хотите узнать подробную информацию или купить билет.")
                $session.date = $parseTree._date.value.substring(0,10);
                $session.date = $session.date.substring(8,10) + "." + $session.date.substring(5,7) + "." + $session.date.substring(0,4)
                for (var id = 1; id < Object.keys(shows).length + 1; id++) {
                    if ($session.date == shows[id].value.date) {
                        var button_name = shows[id].value.title + " в " + shows[id].value.time + " за " + shows[id].value.price;
                        n_buttons = n_buttons + 1;
                        $reactions.inlineButtons({text: button_name, callback_data: id, transition: "/ShowInfo"})
                    }
                }
            }
            
            else {
                n_buttons = -1;
                $reactions.answer("Пожалуйста, укажите дату или выберите фильм.");
            }
            
            if (n_buttons == 0) {
                $reactions.answer("Сеансы не найдены.");    
            }

    state: GetShowId
        event: telegramCallbackQuery
        script:
            $session.show_id = parseInt($request.query);
        go!: /ShowInfo
        
    state: ShowInfo
            script:
                $session.show_id = parseInt($request.query);
            a: Информация о сеансе: \n Фильм "{{ shows[$session.show_id].title}}" \n {{ shows[$session.show_id].format}} \n {{ shows[$session.show_id].date.substring(0,6)}} в {{ shows[$session.show_id].time}} \n Зал {{ shows[$session.show_id].room}} \n Цена: {{ shows[$session.show_id].price}}
            a: Хотите купить билет?
            buttons:
                "да" -> /BuyTicket
                "нет" -> /EndQuery
    
    state: BuyTicket
        a: /переход к покупке билета/
        
    state: EndQuery
        a: Спасибо. Обращайтесь еще.

    state: NoMatch
        event!: noMatch
        a: Извините, я не понял ваш вопрос. Попробуйте переформулировать.