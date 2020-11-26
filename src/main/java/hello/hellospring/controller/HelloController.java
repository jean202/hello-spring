package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model){
            model.addAttribute("data", "hello!!");
            return "hello";
        }

        @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
            model.addAttribute("name", name);
            return "hello-template";
        }
        // API 방식 사용 - view고 model이고 없이 문자가 그냥 내려간다(html 없음)
        @GetMapping("hello-string") // 문자 - 잘 쓰일 일 없다
        @ResponseBody //http 프로토콜의 바디 부분에 직접 넣어 주겠다
    public String helloString(@RequestParam("name") String name){
         return "hello " + name; //"hello whatever"
        }
        // 문자가 아닌 데이터를 내려주겠다
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    static class Hello{
        private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
}
