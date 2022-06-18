package kara.scripts.blood_rune.executor;

public class BankExecutor extends ActivityExecutor {

    private BankActivity localActivity = BankActivity.RETURNING;

    enum BankActivity {
        BANKING,
        RETURNING,
        POTIONING
    }

    @Override
    public int execute() {

        switch (localActivity) {

        }
        return 0;
    }

}
