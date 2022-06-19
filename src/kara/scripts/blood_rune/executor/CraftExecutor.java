package kara.scripts.blood_rune.executor;

public class CraftExecutor extends ActivityExecutor {

    private CraftActivity localActivity = CraftActivity.CRAFT;

    enum CraftActivity {
        CRAFT,
        EXTRACT
    }

    @Override
    public int execute() {

        switch (localActivity) {
            case CRAFT:
                if () {}
                    return ;
            case EXTRACT:
                if () {}
                return ;

        }
        return 0;
    }
}
