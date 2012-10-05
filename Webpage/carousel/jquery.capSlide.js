(function ($) {
    $.fn.capslide = function (options) {
        var opts = $.extend({}, $.fn.capslide.defaults, options);
        return this.each(function () {
            $this = $(this);
            var o = $.meta ? $.extend({}, opts, $this.data()) : opts;

            if (!o.showcaption)    $this.find('.ic_caption').css('display', 'none');
            else $this.find('.ic_text').css('display', 'none');

            var _img = $this.find('img:first');
            var w = _img.css('width');
            var h = _img.css('height');
            $('.ic_caption', $this).css({'color':o.caption_color, 'background-color':o.caption_bgcolor, 'bottom':'0px', 'width':w});
            $('.overlay', $this).css('background-color', o.overlay_bgcolor);
            $this.css({'width':w, 'height':h, 'border':o.border});


            $this.mouseenter(
                function () {

                    $(this).find('.ic_caption').slideDown(300);
                    $('.ic_text', $(this)).slideDown(300);
                }
            );

            $this.mouseleave(
                function () {
                    $(this).find('.ic_caption').slideUp(600);

                    $('.ic_text', $(this)).slideUp(600);
                }
            );


        });
    };
    $.fn.capslide.defaults = {
        caption_color:'white',
        caption_bgcolor:'#305FA1',
        border:'0px solid #fff',
        showcaption:false
    };
})(jQuery);