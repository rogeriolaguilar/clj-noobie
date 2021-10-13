(ns clj_noobie.basics.db)

(def order1 {:user 15
            :items { :mochila { :id :mochila :qtd 2 :unit-price 80}
                     :camiseta { :id :camiseta :qtd 3 :unit-price 40}
                    :tenis { :id :tenis :qtd 1}}})

(def order2 {:user 1
             :items { :mochila { :id :mochila :qtd 22 :unit-price 80}
                     :camiseta { :id :camiseta :qtd 3 :unit-price 40}
                     :tenis { :id :tenis :qtd 1}}})

(def order3 {:user 15
             :items { :mochila { :id :mochila :qtd 3 :unit-price 80}
                     :camiseta { :id :camiseta :qtd 3 :unit-price 40}
                     :tenis { :id :tenis :qtd 1}}})

(def order4 {:user 2
             :items { :mochila { :id :mochila :qtd 7 :unit-price 60}
                     :camiseta { :id :camiseta :qtd 3 :unit-price 40}
                     :tenis { :id :tenis :qtd 1}}})

(def order5 {:user 3
             :items { :mochila { :id :mochila :qtd 2 :unit-price 80}
                     :camiseta { :id :camiseta :qtd 3 :unit-price 40}
                     :tenis { :id :tenis :qtd 1}}})


(defn all-orders []
  [order1 order2 order3 order4 order5])