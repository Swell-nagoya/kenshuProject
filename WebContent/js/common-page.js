"use strict";

jQuery(function ($) {
  // テーブルのheadタグ内 ソート用のリンク表示設定
  new TableSort();

  new StateFlgCheckAll();
});

class TableSort {

  constructor() {
    // ソートの対象となるクラス
    this.table_sort_labelClassName = "js-table_sort_label";
    // ソートの対象となる個別のID名
    this.init();
  }

  init() {
    this.sort_label_height();
    this.event();
  }

  event() {
  //  this.sort();
    $(window).resize(() => {
      this.sort_label_height();
    });
  }

  // 並び替えリンクの高さ調整
  sort_label_height() {

    if ($("." + this.table_sort_labelClassName + " > a").length > 0) {
      $("." + this.table_sort_labelClassName + " > a").css({
        'height': ''
      });
      Promise.resolve().then(() => {
        let sort_label_h = $("." + this.table_sort_labelClassName).height();

        $("." + this.table_sort_labelClassName + " > a").css({
          'height': sort_label_h
        });
      });

    }
  }

}


class StateFlgCheckAll {
  constructor() {
    this.$elements = $(".js-check_all");
    this.elementsLen = this.$elements.length;
    this.dataTarget = "target";
    this.is_fullReleaseClassName = "is-full_release";
    this.selectAll = "全選択"; // ボタンテキスト(利用停止全選択)
    this.fullRelease = "全解除"; // ボタンテキスト(利用停止全解除)
    this.init();
  }
  init() {

    //　
    for (var i = 0; i < this.elementsLen; i++) {
      let $element = $(this.$elements[i]);
      // inputのdata-targetがある場合は、画面内のチェックが入っているか判定.
      // inputを「全選択」or「全解除」の表記に変更
      if ($element.data("target") !== undefined) {
        let targetClassName = $element.data(this.dataTarget);
        let $dataTarget = $("." + targetClassName);

        let targetFlag = true; //全選択の場合はtrue
        for (let z = 0; z < $dataTarget.length; z++) {
          if ($dataTarget[z].checked === false) {
            targetFlag = false;
          }
        }
        // 全選択を適応
        if (targetFlag === true) {
          this.selectorAllFun($element);
          // 全解除を適応
        } else {
          this.fullReleaseFun($element);
        }

        // ターゲットのcheckboxのチェックあり or なし　変動があった場合
        $dataTarget.on("change", (elements) => {
          let $element = $(elements.currentTarget);

          let elementClassName = $element.attr('class');
          let $elementAll = $("." + elementClassName);
          let $dataTarget = $('[data-' + this.dataTarget + '="' + elementClassName + '"]');

          // チェックなしの場合、「全選択」用のボタンに変更
          if ($element[0].checked === false) {

            // 全選択の文言に変更
            for (var i = 0; i < $dataTarget.length; i++) {
              if ($dataTarget[i].value !== this.selectAll) {
                this.fullReleaseFun($($dataTarget[i]));
              }
            }

            // チェックありの場合、全てチェック済みの場合は「全解除」用のボタンに変更
          } else {

            let targetFlag = true; //全選択の場合はtrue
            for (var i = 0; i < $elementAll.length; i++) {
              // チェックなしの時はtrueを代入
              if ($elementAll[i].checked === false) {
                targetFlag = false;

              }
            }
            // 全解除の文言に変更
            if (targetFlag === true) {

              for (var i = 0; i < $dataTarget.length; i++) {
                if ($dataTarget[i].value !== this.fullRelease) {
                  this.selectorAllFun($($dataTarget[i]));
                }
              }
            }
          }
        });
      }
    }

    this.event();

  }

  event() {
    this.$elements.on("click", (element) => {
      let $element = $(element.currentTarget);
      let $dataTarget = $("." + $element.data(this.dataTarget));
      if ($element[0].value === this.selectAll) {
        for (var i = 0; i < $dataTarget.length; i++) {
          const current = $dataTarget[i];
          // チェックなしの時はtrueを代入
          if (current.checked === false) {
            current.checked = true;
          }
        }
        this.selectorAllFun($element);
      } else {
        for (var i = 0; i < $dataTarget.length; i++) {
          const current = $dataTarget[i];
          // チェックありの時はfalseを代入
          if (current.checked === true) {
            current.checked = false;
          }
        }
        this.fullReleaseFun($element);
      }
    });


  }
  // ターゲットのcheckboxをチェックありに変更
  selectorAllFun($element) {
    if (!$element.hasClass(this.is_fullReleaseClassName)) {
      $element.addClass(this.is_fullReleaseClassName);
    }
    $element[0].value = this.fullRelease;
  }
  // ターゲットのcheckboxをチェックなしに変更
  fullReleaseFun($element) {
    if ($element.hasClass(this.is_fullReleaseClassName)) {
      $element.removeClass(this.is_fullReleaseClassName);
    }
    $element[0].value = this.selectAll;
  }
}