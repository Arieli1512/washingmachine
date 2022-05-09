package edu.iis.mto.testreactor.washingmachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WashingMachineTest {

    @Mock
    private DirtDetector dirtDetector;
    @Mock
    private Engine engine;
    @Mock
    private WaterPump waterPump;
    private WashingMachine washingMashine;

    @BeforeEach
    void setUp() {
        washingMashine = new WashingMachine(dirtDetector, engine, waterPump);
    }

    @Test
    void laundryCompletedSuccessfullyTest() {
        Material material = Material.WOOL;
        Program program = Program.SHORT;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(1).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.SUCCESS).withErrorCode(ErrorCode.NO_ERROR).withRunnedProgram(Program.SHORT).build();
        assertEquals(laundryReturnStatus, washingMashine.start(laundryBatch,programConfiguration));
    }

    @Test
    void laundryFailedWithTooHeavyError(){
        Material material = Material.JEANS;
        Program program = Program.MEDIUM;
        LaundryBatch laundryBatch = LaundryBatch.builder().withWeightKg(11).withMaterialType(material).build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder().withProgram(program).build();
        LaundryStatus laundryReturnStatus = LaundryStatus.builder().withResult(Result.FAILURE).withErrorCode(ErrorCode.TOO_HEAVY).withRunnedProgram(null).build();
        assertEquals(laundryReturnStatus, washingMashine.start(laundryBatch,programConfiguration));
    }
}
