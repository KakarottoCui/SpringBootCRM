//微信：egvh56ufy7hh ，QQ：821898835
@SpringBootApplication
@MapperScan("com.study.crm.dao")
@EnableScheduling
public class SpringBootApplicationStart {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplicationStart.class);
    }
}
